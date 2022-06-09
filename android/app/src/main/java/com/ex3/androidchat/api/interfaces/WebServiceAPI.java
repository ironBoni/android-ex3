package com.ex3.androidchat.api.interfaces;

import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.Contact;
import com.ex3.androidchat.models.contacts.ContactRequest;
import com.ex3.androidchat.models.contacts.GetUserDetailsResponse;
import com.ex3.androidchat.models.contacts.MessageResponse;
import com.ex3.androidchat.models.contacts.PutContactRequest;
import com.ex3.androidchat.models.contacts.PutMessageRequest;
import com.ex3.androidchat.models.contacts.SendMessageRequest;
import com.ex3.androidchat.models.contacts.UserModel;
import com.ex3.androidchat.models.login.LoginRequest;
import com.ex3.androidchat.models.login.LoginResponse;
import com.ex3.androidchat.models.login.TokenResponse;
import com.ex3.androidchat.models.register.RegisterRequest;
import com.ex3.androidchat.models.register.UsersList;
import com.ex3.androidchat.models.settings.ChangeServerRequest;
import com.ex3.androidchat.models.transfer.TransferRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WebServiceAPI {
    @GET("contacts")
    Call<List<Contact>> getContacts(@Header("Authorization") String token);

    @GET("contacts/{id}/messages")
    Call<List<MessageResponse>> getMessagesById(@Path("id") String id, @Header("Authorization") String token);

    @GET("contacts/{id}/messages/last")
    Call<MessageResponse> getLastMessage(@Path("id") String id, @Header("Authorization") String token);

    @GET("contacts/{id}/messages/{id2}")
    Call<MessageResponse> getMessagesByIdAndMsgId(@Path("id") String id, @Path("id2") int id2, @Header("Authorization") String token);

    @POST("contacts/{id}/messages")
    Call<Void> sendMessage(@Path("id") String id, @Body SendMessageRequest req, @Header("Authorization") String token);

    @DELETE("contacts/{id}/messages/{id2}")
    Call<Void> removeMessageById(@Path("id") String id, @Path("id2") int id2, @Header("Authorization") String token);

    @PUT("contacts/{id}/messages/{id2}")
    Call<Void> updateMessageById(@Path("id") String id, @Path("id2") int id2, @Body PutMessageRequest req, @Header("Authorization") String token);

    @POST("contacts")
    Call<Void> createContact(@Body ContactRequest contact, @Header("Authorization") String token);

    @GET("contacts/server/{id}")
    Call<GetUserDetailsResponse> getServerByUsername(@Path("id") String id, @Header("Authorization") String token);

    @DELETE("contacts/{id}")
    Call<Void> deleteContact(@Path("id") int id, @Header("Authorization") String token);

    @GET("contacts/{username}")
    Call<Void> getContact(@Path("username") String username, @Header("Authorization") String token);

    @PUT("contacts/{id}")
    Call<Void> putContact(@Path("id") String id, @Body PutContactRequest req, @Header("Authorization") String token);

    @POST("invitations")
    Call<Void> sendInvitation();

    @GET("login/{id}")
    Call<TokenResponse> getToken(@Path("id") String id);

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest req);

    @GET("register")
    Call<UsersList> getRegister();

    @GET("users")
    Call<ArrayList<UserModel>> getUsers(@Header("Authorization") String token);

    @GET("chats")
    Call<List<Chat>> getChats(@Header("Authorization") String token);

    @POST("register")
    Call<Void> register(@Body RegisterRequest req);

    @POST("transfer")
    Call<Void> transfer(@Body TransferRequest req);

    @PUT("users")
    Call<Void> changeSettings(@Body ChangeServerRequest req, @Header("Authorization") String token);
}
