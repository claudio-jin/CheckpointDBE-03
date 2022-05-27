package br.com.fiap.bean;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.primefaces.model.file.UploadedFile;

import br.com.fiap.dao.UserDao;
import br.com.fiap.model.User;

@Named
@RequestScoped
public class UserBean {

	private User user = new User();

	// injeção de dependencia
	// CDI
	@Inject
	private UserDao dao;

	private UploadedFile image;

	public String save() throws IOException {
		
		ServletContext servletContext = (ServletContext) FacesContext
											.getCurrentInstance()
											.getExternalContext()
											.getContext();
		
		String path = servletContext.getRealPath("/");
		
		FileOutputStream out = 
				new FileOutputStream(path + "\\images\\users\\" + image.getFileName());
		
		out.write(image.getContent());
		out.close();
		
		user.setImagePath("\\images\\users\\" + image.getFileName());
		dao.insert(user);
		
		mostrarMensagem("Usuario cadastrado com sucesso");
		
		return "login?faces-redirect=true";
	}

//	public String save() {
//
//		dao.insert(user);
//
//		mostrarMensagem("Usuario cadastrado");
//
//		return "login?faces-redirect=true";
//	}

	private void mostrarMensagem(String msg) {
		FacesContext.getCurrentInstance()
			.getExternalContext()
			.getFlash()
			.setKeepMessages(true);

		FacesContext.getCurrentInstance()
			.addMessage(null, new FacesMessage(msg));
	}

	public List<User> getUsers() {
		return dao.list();
	}

	public String login() {

		if (dao.exist(user)) {
			FacesContext.getCurrentInstance()
				.getExternalContext()
				.getSessionMap()
				.put("user", user);

			return "setups";
		}

		mostrarMensagem("Login inválido");

		return "login?faces-redirect=true";

	}

	public String logout() {
		FacesContext.getCurrentInstance()
			.getExternalContext()
			.getSessionMap()
			.remove("user");

		return "login?faces-redirect=true";
	}
	
	//Atualizar usuario
	public void update() {
		dao.update(user);
		mostrarMensagem("Usuario atualizado com sucesso");
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public UploadedFile getImage() {
		return image;
	}

	public void setImage(UploadedFile image) {
		this.image = image;
	}

}
