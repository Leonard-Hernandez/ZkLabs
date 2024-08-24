package profile;

import java.io.Serializable;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.util.Clients;

import entity.User;
import services.AuthenticationService;
import services.CommonInfoService;
import services.UserCredential;
import services.UserInfoService;

public class ProfileViewModel implements Serializable {

	// services
	private AuthenticationService authService = new AuthenticationServiceChapter3Impl();
	private UserInfoService userInfoService = new UserInfoServiceChapter3Impl();

	private User currentUser;

	public ProfileViewModel() {
		UserCredential userCredential = authService.getUserCredential();
		currentUser = userInfoService.findUser(userCredential.getAccount());
		if (currentUser == null) {
			// TODO handle un-authenticated access
			return;
		}
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public List<String> getCountryList() {

		return CommonInfoService.getCountryList();
	}
	
	@Command
	@NotifyChange("currentUser")
	public void save() {
		currentUser = userInfoService.updateUser(currentUser);
		Clients.showNotification("Your Profile is updated");
	}
	
	@Command
	@NotifyChange("currentUser")
	public void reload() {
		UserCredential cre = authService.getUserCredential();
		currentUser = userInfoService.findUser(cre.getAccount());
	}

}
