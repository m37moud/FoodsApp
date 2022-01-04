package com.example.foods.requests.responses;

import java.io.IOException;

import retrofit2.Response;
/**
 * Generic class for handling responses from Retrofit
 * @param <T>
 */

public class ApiResponse<T> {


    public ApiResponse<T> create(Response<T> response){
       if(response.isSuccessful()){
           T body = response.body();
           if(body == null || response.code() == 204){ // 204 error code mean response sucsessful but is empty
               return new ApiEmptyResponse<>();

           }else {
               return new ApiSuccessResponse<>(body);
           }

       }
       else{
           String error;
           try{
               error = response.errorBody().string();

           }catch (IOException e){
               e.printStackTrace();
               error = response.message();
           }
           return new ApiErrorResponse<>(error);
       }

    }

    public ApiResponse<T> create(Throwable errorMSG){
        return new ApiErrorResponse<>(!errorMSG.getMessage().equals("") ? errorMSG.getMessage() : "Unknown ERROR\nCheck network connection");
    }

    /**
     * Generic success response from api
     * @param <T>
     */

    public class ApiSuccessResponse<T> extends ApiResponse<T>{
        private T body;
        ApiSuccessResponse( T body ){
            this.body =body;

        }
        public T getBody(){
            return body;
        }

    }
    /**
     * Generic Error response from API
     * @param <T> can be int Sting or anny type
     */

    public class ApiErrorResponse<T> extends ApiResponse<T>{

        private String errorMSG;
        ApiErrorResponse(String errorMSG){
            this.errorMSG = errorMSG;

        }

        public String getErrorMessage(){
            return errorMSG;
        }

    }

    /**
     * separate class for HTTP 204 resposes so that we can make ApiSuccessResponse's body non-null.
     */
    public class ApiEmptyResponse<T>extends ApiResponse<T>{

    }

}
