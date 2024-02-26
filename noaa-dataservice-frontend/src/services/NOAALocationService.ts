import {NOAALocation} from "../models/NOAALocation";
import axios from "axios";

const apiPath = 'http://localhost:8080'

export const getRemoteLocations = async (locationCategoryId:string) => {
    const response = await axios.get<NOAALocation[]>(`${apiPath}/NOAA/location/remote`,{params:{locationCategoryId:locationCategoryId}});
    return response.data;
}

export const getLocalLocations = async (locationCategoryId:string) => {
    const response = await axios.get<NOAALocation[]>(`${apiPath}/NOAA/location`,{params:{locationCategoryId:locationCategoryId}});
    return response.data;
}

export const loadLocationsByIds = async (locationCategoryId:string, locationsIds:string[]) => {
    await axios.put(`${apiPath}/NOAA/location/load`, locationsIds, {params:{locationCategoryId:locationCategoryId}});
}

export const deleteLocationsByIds = async (ids:string[]) => {
    await axios.delete(`${apiPath}/NOAA/location/deleteByIds`, {data:ids})
}