import {NOAALocation} from "../models/NOAALocation";
import axios from "axios";

const apiPath = 'http://localhost:8080'

export const getAllLocalCountries = async ():Promise<NOAALocation[]> => {
    const response = await axios.get<NOAALocation[]>(`${apiPath}/NOAA/location/countries`);
    return response.data;
}

export const getAllLocalCities = async ():Promise<NOAALocation[]> => {
    const response = await axios.get<NOAALocation[]>(`${apiPath}/NOAA/location/cities`);
    return response.data;
}

export const getAllLocalStates = async ():Promise<NOAALocation[]> => {
    const response = await axios.get<NOAALocation[]>(`${apiPath}/NOAA/location/states`);
    return response.data;
}

export const getAllRemoteCountries = async ():Promise<NOAALocation[]> => {
    const response = await axios.get<NOAALocation[]>(`${apiPath}/NOAA/location/countries/remote`);
    return response.data;
}

export const getAllRemoteCities = async ():Promise<NOAALocation[]> => {
    const response = await axios.get<NOAALocation[]>(`${apiPath}/NOAA/location/cities/remote`);
    return response.data;
}

export const getAllRemoteStates = async ():Promise<NOAALocation[]> => {
    const response = await axios.get<NOAALocation[]>(`${apiPath}/NOAA/location/states/remote`);
    return response.data;
}
