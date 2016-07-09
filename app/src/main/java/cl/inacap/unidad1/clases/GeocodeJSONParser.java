package cl.inacap.unidad1.clases;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GeocodeJSONParser {

    /** recive un JSONObject y retorna una lista*/
    public List<HashMap<String,String>> parse(JSONObject jObject){

        JSONArray jPlaces = null;
        try {
            /** recupera todos los elementos en el array "places" */
            jPlaces = jObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /* invoca "getPlaces" con el array del objeto
          donde cada uno representa un lugar
         */
        return getPlaces(jPlaces);
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jPlaces){
        int placesCount = jPlaces.length();
        List<HashMap<String, String>> placesList = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> place = null;

        /** tomando todos los lugares, transformandolos y agregandolo a una lista*/
        for(int i=0; i<placesCount;i++){
            try {
                /** llama  a "getPlace"  en los Json para pasarlos al lugar*/
                place = getPlace((JSONObject)jPlaces.get(i));
                placesList.add(place);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return placesList;
    }

    /** transforma el lugar del objeto del json*/
    private HashMap<String, String> getPlace(JSONObject jPlace){

        HashMap<String, String> place = new HashMap<String, String>();
        String formatted_address = "-NA-";
        String lat="";
        String lng="";

        try {
            // extrae la direccion formateada si esta disponible
            if(!jPlace.isNull("formatted_address")){
                formatted_address = jPlace.getString("formatted_address");
            }

            lat = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat");
            lng = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng");

            place.put("formatted_address", formatted_address);
            place.put("lat", lat);
            place.put("lng", lng);

        }catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }
}
