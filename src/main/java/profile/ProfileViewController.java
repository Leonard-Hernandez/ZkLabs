package profile;

import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

import entity.User;
import services.AuthenticationService;
import services.CommonInfoService;
import services.UserCredential;
import services.UserInfoService;

public class ProfileViewController extends SelectorComposer<Component> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// wire components
	@Wire
	Label account;
	@Wire
	Textbox fullName;
	@Wire
	Textbox email;
	@Wire
	Datebox birthday;
	@Wire
	Listbox country;
	@Wire
	Textbox bio;

	// services
	AuthenticationService authService = new AuthenticationServiceChapter3Impl();
	UserInfoService userInfoService = new UserInfoServiceChapter3Impl();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		ListModelList<String> countryModel = new ListModelList<String>(CommonInfoService.getCountryList());
		country.setModel(countryModel);
		refreshProfileView();
	}

	@Listen("onClick=#saveProfile")
	public void soSaveProfile() {
		UserCredential userCredential = authService.getUserCredential();
		User user = userInfoService.findUser(userCredential.getAccount());
		if (user == null) {
			// TODO handle un-authenticated access
			return;
		}

		// apply componet value to bean;
		user.setFullName(fullName.getValue());
		user.setEmail(email.getValue());
		user.setBirthday(birthday.getValue());
		user.setBio(bio.getValue());

		Set<String> selection = ((ListModelList) country.getModel()).getSelection();
		System.out.println(selection);
		if (!selection.isEmpty()) {
			user.setCountry(selection.iterator().next());
			System.out.println(selection.iterator().next());
		} else {
			user.setCountry(null);
		}

		userInfoService.updateUser(user);

		Clients.showNotification("Your Profile is updated");

	}
	
	

	@Listen("onClick=#reloadProfile")
	public void doReloadProfile() {
		refreshProfileView();
	}

	private void refreshProfileView() {
		UserCredential userCredential = authService.getUserCredential();
		User user = userInfoService.findUser(userCredential.getAccount());
		if (user == null) {
			// TODO handle un-authenticated access
			return;
		}

		// apply bean value to UI components
		account.setValue(user.getAccount());
		fullName.setValue(user.getFullName());
		email.setValue(user.getEmail());
		birthday.setValue(user.getBirthday());
		bio.setValue(user.getBio());

		((ListModelList) country.getModel()).addToSelection(user.getCountry());
	}

}
