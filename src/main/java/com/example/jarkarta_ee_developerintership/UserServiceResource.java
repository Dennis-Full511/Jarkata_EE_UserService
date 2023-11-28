package com.example.jarkarta_ee_developerintership;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import java.security.MessageDigest;


@Path("/UserService")
public class UserServiceResource {
    private static List<User> userList = new ArrayList<>();
    @POST
    @Path("/createUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String createUser(User newUser){
        String res = "Error: Id already exists";
        //newUser = hashPassword(newUser);
        if (getUserFromId(newUser.getId()) == null){
            res = "Success";
            userList.add(newUser);
        }
        return res;
    }

    @POST
    @Path("/changeUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String changeUser(User user){
        String res = "Error: No Such User";
        User LocalUser = getUserFromId(user.getId());
        if (LocalUser != null){
            //Change attributes of local user
            LocalUser.setFirstname(user.getFirstname());
            LocalUser.setLastname(user.getLastname());
            LocalUser.setBirthday(user.getBirthday());
            LocalUser.setEmail(user.getEmail());
            LocalUser.setPassword(user.getPassword());
            res = "Success";
        }
        return res;
    }

    private User getUserFromId(int id){
        User res = null;
        for (int i = 0;i < userList.size();i++){
            User tmp = userList.get(i);
            if (tmp.getId() == id){
                res = tmp;
                break;
            }
        }
        return res;
    }

    private User hashPassword(User user){
        String newPW = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            //newPW = messageDigest.digest(user.getPassword().getBytes());
        }catch (NoSuchAlgorithmException ignored){

        }


        return user;
    }
}
