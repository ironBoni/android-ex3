package com.ex3.androidchat.services;

import com.ex3.androidchat.AndroidChat;
import com.ex3.androidchat.R;
import com.ex3.androidchat.api.interfaces.WebServiceAPI;
import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.Contact;
import com.ex3.androidchat.models.User;
import com.ex3.androidchat.models.contacts.UserModel;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserService implements  IUserService {
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public UserService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(AndroidChat.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    private static ArrayList<User> users = new ArrayList<>();
/*            new User("idan", "Idan Ben Ari", "Np1234", "https://fiverr-res.cloudinary.com/images/t_main1,q_auto,f_auto,q_auto,f_auto/attachments/delivery/asset/54164cf6ae1512d8c0a2b1d8306c5a68-1649285147/wouterbult/draw-nice-style-cartoon-caricature-as-a-profile-picture.jpg"),
            new User("noam", "Noam Cohen", "Np1234", "https://scontent.fsdv3-1.fna.fbcdn.net/v/t1.6435-9/46498020_2215436798469037_9121585456583016448_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=pgAp58x8fUcAX_q7Geu&_nc_ht=scontent.fsdv3-1.fna&oh=00_AT-sSFofwqgtb84VAAwlKpm5m_LtaBWKsq04MI4ZqVYu_A&oe=62C19375"),
            new User("ran", "Ran Levy", "Np1234", "https://i.etsystatic.com/28761236/c/2000/1589/0/76/il/d929f2/3162035768/il_340x270.3162035768_ci09.jpg"),
            new User("ron", "Ron Solomon", "Np1234", "https://cdn.dribbble.com/users/2364329/screenshots/10481283/media/f013d5235bfcf1753d56cad154f11a67.jpg"),
            new User("dvir", "Dvir Pollak", "Np1234", "https://media-exp1.licdn.com/dms/image/C4E03AQF6ZOFmppSpxg/profile-displayphoto-shrink_200_200/0/1646299616015?e=1660176000&v=beta&t=wnbQBn76v397qnw3fWuHpQD2ocgcI6pAAL06XXbsw_I"),
            new User("hadar", "Hadar Pinto", "Np1234", "https://media-exp1.licdn.com/dms/image/C4D03AQG7Oph-nHMJdQ/profile-displayphoto-shrink_200_200/0/1646846030443?e=1660176000&v=beta&t=TO5w-Fpy7_ve4_ixX4EWvqZDC1W_A0aqiszvCKT86jo"),
            new User("shlomo", "Shlomo Levin", "Np1234", "https://fiverr-res.cloudinary.com/images/t_main1,q_auto,f_auto,q_auto,f_auto/gigs/50183164/original/f80714a807d09df88dc708d83941384ac5d9e6dd/draw-nice-style-cartoon-caricature-as-a-profile-picture.png"),
            new User("dan", "Dan Cohen", "Np1234", "https://avatoon.net/wp-content/uploads/2020/04/Gary-Avatar.png"),
            new User("oren", "Oren Orbach", "Np1234", "https://i.etsystatic.com/28761236/c/2000/1589/0/76/il/d929f2/3162035768/il_340x270.3162035768_ci09.jpg"),
            new User("yuval", "Yuval Baruchi", "Np1234", "https://cdn.dribbble.com/users/2364329/screenshots/10481283/media/f013d5235bfcf1753d56cad154f11a67.jpg"),
            new User("yaniv", "Yaniv Hoffman", "Np1234", "https://cdn.pixabay.com/photo/2016/12/07/21/01/cartoon-1890438_960_720.jpg")
    ));*/

    public static void setUsers(ArrayList<UserModel> list) {
        users.clear();
        for(UserModel model : list) {
            User user = new User(model.getId(), model.getPassword(), null, model.getProfileImage(), model.getName(),
                    model.getServer(), null);
            users.add(user);
        }
    }

    public static ArrayList<User> getUsers() {
        return users;
    }
    private static IChatService chatsService = new ChatService();

    public String getOtherUserIdInChat(Chat chat, String firstId) {
        for(String participant: chat.getParticipants()) {
            if(participant != firstId)
                return participant;
        }
        return firstId;
    }

    public boolean isContactOf(User user, String secondId) {
        User otherUser = getById(secondId);
        for(Contact contact : user.getContacts()) {
            if(contact.getContactId() == secondId)
                return true;
        }
        return false;
    }

    /*public void loadContacts() {
        ChatService chatService = new ChatService();
        for(UserModel user : getAll()) {
            UserModel otherUser;
            for (Chat chat : chatService.GetAll()) {
                if(chat.getParticipants().contains(user.getId())) {
                    String secondId = getOtherUserIdInChat(chat, user.getId());
                    otherUser = getById(secondId);
                    if(!isContactOf(user, secondId))
                        addContact(user.getId(), secondId, otherUser.getName(), otherUser.getServer(), otherUser.getLast(), otherUser.getLastdate(), otherUser.getProfileImage());
                    if(!isContactOf(otherUser, user.getId()))
                        addContact(otherUser.getId(), user.getId(), user.getName(), user.getServer(), user.getLast(), user.getLastdate(), user.getProfileImage());
                }
            }
        }*/
    //}
    @Override
    public ArrayList<Contact> getContacts(String username) {
        User user = getById(username);
        if(user == null) return null;
        return user.getContacts();
    }

    @Override
    public boolean addContact(String addTo, String id, String name, String server, String last, String lastDate, String profileImage) {
        User addToUser = getById(addTo);
        if(addToUser == null) return false;
        return addToUser.getContacts().add(new Contact(id, name, server, last, profileImage, lastDate));
    }

    @Override
    public boolean removeContact(String username) {
        ArrayList<Contact> contacts = getContacts(username);
        if(contacts == null) return false;
        Contact contactToRemove = null;
        
        for(Contact contact : contacts) {
            if(contact.getContactId().equals(username)) {
                contactToRemove = contact;        
            }
        }
        
        if(contactToRemove == null)
            return false;
        return contacts.remove(contactToRemove);
    }

    @Override
    public ArrayList<User> getAll() {
        return users;
    }

    @Override
    public User getById(String id) {
        for(User user : users) {
            if(user.getId().equals(id))
                return user;
        }
        return null;
    }

    @Override
    public boolean create(User user) {
        return users.add(user);
    }

    @Override
    public boolean update(User user) {
        delete(user.getId());
        return create(user);
    }

    @Override
    public boolean delete(String userId) {
        for (User user : users) {
            if (user.getId() == userId)
                users.remove(user);
            return true;
        }
        return false;
    }


    public String getFullServerUrl(String url)
    {
        if (!url.endsWith("/"))
            url = url + "/";
        if (!url.startsWith("http://"))
            url = "http://" + url;
        return url;
    }

   /* @Override
    public boolean isLoginOk(String username, String password) {
        Call<LoginResponse> call = webServiceAPI.login(new LoginRequest(username, password));
        call.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.body().isCorrectInput())
                    return true;
                return false;
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("retorfit", t.getMessage());
            }
        });
    }*/
}
