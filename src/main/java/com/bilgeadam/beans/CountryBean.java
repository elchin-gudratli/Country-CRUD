package com.bilgeadam.beans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.dao.DataAccessException;

import com.bilgeadam.model.Country;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.*;

@SuppressWarnings("serial")
@ManagedBean(name = "CountryBean")
@SessionScoped
public class CountryBean implements Serializable {
	
    private static final String PRODUCTURL = 
    		"http://52.117.236.126:8085/SpringRestHibernateExample/";
    private  List<Country> countryList = new ArrayList<Country>();
    public CountryBean() {
    }

	private String name;
    private long population;

	private Country SeciliCountry = new Country();
    
  
    @PostConstruct
    public void init()
    {
    	countryList = ulkeleriYukle();
    }
    
    @SuppressWarnings({ "resource", "deprecation" })
	public String addCountry()
	{
		try
		{
	        JSONObject country = new JSONObject();
	        country.put("name", getName()); 
	        country.put("population", getPopulation()); 
	        
	        HttpClient httpClient = new DefaultHttpClient();
	        HttpPost post = new 
	        	//	HttpPost("http://52.117.236.126:8080/SpringRestHibernateExample/addCountry/{name:ibarih,population:5555}");
	        
	        HttpPost(PRODUCTURL+"addCountry/");
	        		Country eklenenCountry;
	        try {
	        	
	        	StringEntity postingString = new StringEntity(country.toString());
	        	post.setEntity(postingString);
	        	post.addHeader("content-type", "application/json");
	        	
	        	HttpResponse  response = httpClient.execute(post);
	            HttpEntity birim = response.getEntity();
	            if (birim != null) {
	                InputStream gelenVeri = birim.getContent();
	                String sonuc = convertStreamToString(gelenVeri);
	             
	                JSONObject jsonObject= new JSONObject(sonuc); 
	                eklenenCountry = new Country();
	                
	                eklenenCountry.setCountryName(jsonObject.get("name").toString());
	                eklenenCountry.setPopulation(Long.valueOf(jsonObject.get("population").toString()));
	                gelenVeri.close();
	                ulkeleriYukle();
	            }
	        } catch (Exception e) {
	          
	        }
	        
	        
			return "index";
		}
		catch (DataAccessException e) 
		{
			e.printStackTrace();
		}
		return "CreateCountry";
	}
    
    @SuppressWarnings({ "resource", "deprecation" })
   
	public List<Country> ulkeleriYukle() {
        HttpClient webIstemci = new DefaultHttpClient();
        HttpGet webdenGetir = new HttpGet(PRODUCTURL+"getAllCountries/");
        HttpResponse donenCevap;
        try {
            donenCevap = webIstemci.execute(webdenGetir);
            HttpEntity birim = donenCevap.getEntity();
            if (birim != null) {
                InputStream gelenVeri = birim.getContent();
                String sonuc = convertStreamToString(gelenVeri);
                
                JSONArray jsonArray = new JSONArray(sonuc); 
                Country country = null;
                countryList.clear();//gson 
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject explrObject = jsonArray.getJSONObject(i);
                    country = new Country();
                    country.setId(Integer.valueOf(explrObject.get("id").toString()));
                    country.setCountryName(explrObject.get("name").toString());
                    country.setPopulation(Long.valueOf(explrObject.get("population").toString())); 
                    country.setImageUrl(explrObject.get("imageUrl").toString());
                    countryList.add(country);
                }
                gelenVeri.close();
            }
        } catch (Exception e) {
          
        }
        return countryList;
    }
    @SuppressWarnings({ "resource", "deprecation" })
		
    public String UlkeDetayGetir(Country country) 
         {
	        HttpClient webIstemci = new DefaultHttpClient();
	        HttpGet webdenGetir = new HttpGet(PRODUCTURL+"getCountry/"+country.getId());
	        HttpResponse donenCevap;
	        try {
	            donenCevap = webIstemci.execute(webdenGetir);
	            HttpEntity birim = donenCevap.getEntity();
	            if (birim != null) {
	                InputStream gelenVeri = birim.getContent();
	                String sonuc = convertStreamToString(gelenVeri);
	                JSONObject jsonObject= new JSONObject(sonuc); 
	                SeciliCountry.setCountryName(jsonObject.get("name").toString());
	                SeciliCountry.setPopulation(Long.valueOf(jsonObject.get("population").toString()));

	                gelenVeri.close();
	            }
	        } catch (Exception e) {
	        	
	          return("index");
	        }
	 
			return "DetailsCountry";

	    }

    public String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        
        return sb.toString();
    }

	public List<Country> getCountryList() {
		return countryList;
	}

	public void setCountryList(List<Country> countryList) {
		this.countryList = countryList;
	}

	public static String getProducturl() {
		return PRODUCTURL;
	}


    public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public long getPopulation() {
		return population;
	}


	public void setPopulation(long population) {
		this.population = population;
	}

	public Country getSeciliCountry() {
			return SeciliCountry;
		}

	public void setSeciliCountry(Country seciliCountry) {
			SeciliCountry = seciliCountry;
		}

	
 
}