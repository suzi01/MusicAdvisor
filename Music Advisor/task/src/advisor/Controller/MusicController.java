package advisor.Controller;

import advisor.Authorization;
import advisor.Model.UserDTO;
import advisor.View.MusicView;

import java.io.IOException;

public class MusicController {
    private final UserDTO user;
    private final MusicView musicView;


    public MusicController(UserDTO user, MusicView musicView) {
        this.user = user;
        this.musicView = musicView;
    }

    public void incorrectResponse(){
        musicView.incorrectAccess();
    }

    public void userAccessExit(){
        musicView.userAccessExit();
    }

    public void getAuthCode() throws IOException, InterruptedException {
        user.setAccessCode(Authorization.auth());
    }

    public String getUserAuthCode() throws IOException, InterruptedException {
        return user.getAccessCode();
    }

    public void getAccessToken(String authCode, String accessServer) throws IOException, InterruptedException {
        user.setToken(Authorization.accessToken(authCode, accessServer));
    }

    public String getUserAccessToken() throws IOException, InterruptedException {
        return user.getToken();
    }


    public void getNewList(String serverPath, int pageNumber) throws IOException, InterruptedException {
        musicView.newList(user.getToken(), serverPath, pageNumber);
    }

    public void getFeaturedMusic(String serverPath, int pageNumber) throws IOException, InterruptedException {
        musicView.featured(user.getToken(), serverPath, pageNumber);
    }

    public void getCategories(String serverPath, int pageNumber) throws IOException, InterruptedException {
        musicView.categories(user.getToken(), serverPath, pageNumber);
    }

    public void getPlaylists(String word, String serverPath, int pageNumber) throws IOException, InterruptedException {
        musicView.playLists(word, user.getToken(), serverPath, pageNumber);
    }


    public void exit(){
        musicView.exit();
    }

}
