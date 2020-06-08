package ir.aminer.potadoshack.client.page;

import ir.aminer.potadoshack.client.PotadoShack;
import ir.aminer.potadoshack.client.User;
import ir.aminer.potadoshack.client.page.callbacks.IPageEnter;
import ir.aminer.potadoshack.client.page.callbacks.IPageError;
import ir.aminer.potadoshack.client.page.callbacks.IPageExit;
import ir.aminer.potadoshack.client.page.callbacks.IPageResponse;

import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.ErrorPacket;
import ir.aminer.potadoshack.core.network.packets.ResponsePacket;
import ir.aminer.potadoshack.core.error.Error;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.EOFException;
import java.io.IOException;

public class Page implements IPageEnter, IPageExit, IPageResponse, IPageError {
    private final Parent parent;

    // TODO: Rework constructors
//
//    public Page() {
//        this((Parent) null);
//    }

    public Page(String path) {
        Parent temp_parent;
        try {
            FXMLLoader loader = new FXMLLoader(PotadoShack.class.getResource(path));
            loader.setController(this);
            temp_parent = loader.load();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.err.println("Could not load: " + path);
            temp_parent = null;
        }
        this.parent = temp_parent;
    }

    public Page(Parent parent) {
        this.parent = parent;
    }

    public Parent getParent() {
        return parent;
    }

    public void handleResponse(ClientSocket client) throws IOException, ClassNotFoundException {
        try {
            ResponsePacket response = client.readResponse();

            if (response.getStatus().equals(ResponsePacket.Status.ERROR)) {
                Error error = ((ErrorPacket) response).getError();

                if (error.equals(Error.UNAUTHORIZED_TOKEN) || error.equals(Error.INVALID_TOKEN))
                    onAuthorityError(error);

                onError(error);
            } else if (response.getStatus().equals(ResponsePacket.Status.OK))
                onResponse(response);
        } catch (EOFException eofException) {
            System.out.println(client.getAddress() + "#No responses were received");
        }
    }

    public void onResponse(ResponsePacket response) throws IOException {
    }

    public void onError(Error error) throws IOException {
    }

    public void onEnter(Page from) {
    }

    public void onExit(Page to) {
    }

    public void onAuthorityError(Error error) throws IOException {
        if (User.getPreferenceFile().delete()){
            PageHandler.getInstance().activePage("sign_in");
        }
    }
}