package com.example.jarkarta_ee_developerintership;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
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
    public Response createUser(simpleUser simpleUser){
        Response res;
        User newUser;
        try {
            newUser = convertUser(simpleUser);
        }catch (NoSuchAlgorithmException e){
         return Response.serverError().build();
        }
        if (getUserFromId(newUser.getId()) == null){
            userList.add(newUser);
            res = Response.ok("Data updated!").build();
        }else res = Response.serverError().build();
        return res;
    }

    /*
    @POST
    @Path("/changeUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response changeUser(simpleUser simpleUser){
        Response res;
        User newUser = convertUser(simpleUser);
        User localUser = getUserFromId(newUser.getId());
        if (localUser != null){
            //Change attributes of local user
            localUser.setFirstname(newUser.getFirstname());
            localUser.setLastname(newUser.getLastname());
            localUser.setBirthday(newUser.getBirthday());
            localUser.setEmail(newUser.getEmail());
            localUser.setPassword(newUser.getPassword());
            res = Response.ok("Data updated!").build();
        }else res = Response.serverError().build();
        return res;
    }
    */
    @POST
    @Path("/changeUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response changeUser(ChangeUserRequest change){
        Response res;
        User localUser = getUserFromId(change.getId());
        if (localUser != null){
            //Change attribute of local user
            switch (change.getAttribute()){
                case "firstname":
                    localUser.setFirstname(change.getValue());
                    break;
                case "lastname":
                    localUser.setLastname(change.getValue());
                    break;
                case "email":
                    localUser.setEmail(change.getValue());
                    break;
                case "birthday":
                    localUser.setBirthday(LocalDate.parse(change.getValue()));
                    break;
                case "password":
                    try {
                        localUser.setPassword(hashPassword(change.getValue()));
                    }catch (NoSuchAlgorithmException e){
                        return Response.serverError().build();
                    }

                    break;
            }
            res = Response.ok("Data updated!").build();
        }else res = Response.serverError().build();
        return res;
    }

    @POST
    @Path("/deleteUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response delteUser(simpleUser simpleUser){
        return Response.serverError().build();
    }

    @POST
    @Path("/getUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(int id){
        return Response.serverError().build();
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

    private User convertUser(simpleUser simpleUser) throws NoSuchAlgorithmException {
        return new User(simpleUser.getFirstname(),simpleUser.getLastname(),simpleUser.getEmail(), LocalDate.parse(simpleUser.getBirthday()),hashPassword(simpleUser.getPassword()),simpleUser.getId());
    }
    private byte[] hashPassword(String string) throws NoSuchAlgorithmException {
        byte[] newPW = null;
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        newPW = messageDigest.digest(string.getBytes());
        return newPW;
    }
}
