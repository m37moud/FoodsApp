package com.example.foods.util;

import androidx.lifecycle.LiveData;

import com.example.foods.requests.responses.ApiResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class LiveDataCallAdapterFactory extends CallAdapter.Factory {
    /**
     * the method should check for every parameter inside live data then return the type as retrofit request
     * it can be RecipeResponse.class or RecipeSearchResponse .
     *
     *
     * @param returnType
     * @param annotations
     * @param retrofit
     * @return
     */

    /*
    * check 1) check from the type which we want to change to ( we need to change to live data so we will check this type is livedata.class
    * check2 ) check from the type inside livedata<T> (check (T) is ApiResponse.class or not
    * check 3 ) check from type of response inside ApiResponse<T>
    *
     */
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {

        //remember we need to convert to this LiveData<ApiResponse<RecipeResponse>>
        // check 1 # check from the return type it sholud be Livedata.class
       if (CallAdapter.Factory.getRawType(returnType) != LiveData.class)
        {
            return null;
        }
       //check 2) : 2.1 : first we get the type inside LiveData<T> @T is should be ApiResponse
        Type observaleType = CallAdapter.Factory.getParameterUpperBound(0 , (ParameterizedType)returnType); // extract the generic paramiter this back ApiResponse
        Type rawObservaleType = CallAdapter.Factory.getRawType(observaleType); // extract Raw class back the type but .class
       //2.2 check if it ApiResponse.class
        if(rawObservaleType != ApiResponse.class){
           throw  new IllegalArgumentException("Wrong Type Resouse ");
        }
        //check 3 )3.1 second we check if it parametrized mean it contain T EXE : ApiResponse<T> got the second @<T>it will be RecipeResponse.class or RecipeSearchResponse

        //3.2 check if it will be RecipeResponse.class or RecipeSearchResponse
        if(!(observaleType instanceof  ParameterizedType)){
           throw new IllegalArgumentException("Wrong Type Resouse ");
        }
        //3.2 check if it will be RecipeResponse.class or RecipeSearchResponse
      //  Type bodyType = CallAdapter.Factory.getRawType(observaleType);
        Type bodyType = CallAdapter.Factory.getParameterUpperBound(0,(ParameterizedType)observaleType);

        return new LiveDataCallAdapter<Type>(bodyType);
    }
}
