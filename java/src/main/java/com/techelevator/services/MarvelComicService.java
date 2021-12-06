package com.techelevator.services;

import com.techelevator.model.Comic;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MarvelComicService {

    private String API_BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    //Need to see what else the service needs to connect to the Marvel API
    //From https://developer.marvel.com/documentation/authorization
    //Server-side applications must pass two parameters in addition to the apikey parameter:
    //ts- a timestamp (or other long string which can change on a request-by-request basis)
    //hash - a md5 digest of the ts parameter, your private key and your public key (e.g. md5(ts+privateKey+publicKey)
    //ex) http://gateway.marvel.com/v1/public/comics?ts=1&apikey=1234&hash=ffd275c5130566a2916217b101f26150 (the hash value is the md5 digest of 1abcd1234)
    private String privateKey;
    private String publicKey;
    private Long timeStamp;


    public MarvelComicService(String API_BASE_URL, String privateKey,String publicKey){
        this.API_BASE_URL = API_BASE_URL;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.timeStamp = (long)1;
    }
/*
    public Comic getComic(long comicId){
        try{
            ResponseEntity<Comic> response =
                    restTemplate.exchange(API_BASE_URL+"");
        }
    }*/



    //This function needs to be called to get the timestamp, private key, public key, and hash of (ts + private key + public key)
    //These items are required to make a call to the marvel API from a backend server
    //They are returned in a List size 4 where element order is [0] = timestamp, [1] = private key, [2] = public key, [3] = hash of all three
    public List<String> generateAuthInfo(String privateKey, String publicKey){

        List<String> listOfAuthInfo=new ArrayList<String>();
        timeStamp += 1; //global timestamp auto incremented each time function is called, Ie. a request to marvel api is made
        listOfAuthInfo.add(String.valueOf(timeStamp));
        listOfAuthInfo.add(privateKey);
        listOfAuthInfo.add(publicKey);

        //Generate md5 hash
        try {
            String hashString = timeStamp+privateKey+publicKey;

            byte[] bytesOfMessage = hashString.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] theMD5digest = md.digest(bytesOfMessage);

            String MD5ByteToString = new String(theMD5digest);
            listOfAuthInfo.add(MD5ByteToString);

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("generateAuthInfo in MarvelComicService failed at generating hash");
        }

        return listOfAuthInfo;

    }

}
