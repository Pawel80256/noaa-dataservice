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

export const loadCountriesByIds = async (ids:string[]) => {
    await axios.put(`${apiPath}/NOAA/location/countries/loadByIds`, ids);
}

export const loadAllCountries = async () => {
    await axios.put(`${apiPath}/NOAA/location/countries/load`);
}

export const loadCitiesByIds = async (ids:string[]) => {
    await axios.put(`${apiPath}/NOAA/location/cities/loadByIds`, ids);
}

export const loadStatesByIds = async (ids:string[]) => {
    await axios.put(`${apiPath}/NOAA/location/states/loadByIds`, ids);
}

export const loadAllStates = async () => {
    await axios.put(`${apiPath}/NOAA/location/states/load`);
}

export const deleteLocationsByIds = async (ids:string[]) => {
    await axios.delete(`${apiPath}/NOAA/location/deleteByIds`, {data:ids})
}