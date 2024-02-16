import {NOAADataType} from "../models/NOAADataType";
import axios from "axios";
import {NOAALocationCategory} from "../models/NOAALocationCategory";

const apiPath = 'http://localhost:8080'

export const getAllLocalLocationCategories = async ():Promise<NOAALocationCategory[]> => {
    const response = await axios.get<NOAALocationCategory[]>(`${apiPath}/NOAA/locationcategory`);
    return response.data;
}

export const getAllRemoteLocationCategories = async (): Promise<NOAADataType[]> => {
    const response = await axios.get<NOAADataType[]>(`${apiPath}/NOAA/locationcategory/remote`);
    return response.data;
};

export const loadLocationCategoriesByIds = async (ids:string[], singly:boolean) => {
    await axios.put(`${apiPath}/NOAA/locationcategory/loadByIds`, ids, {params: {singly:singly}})
}

export const loadAllLocationCategories = async () => {
    await axios.put(`${apiPath}/NOAA/locationcategory/load`);
}

export const deleteLocalLocationCategoriesByIds = async(ids:string[]) => {
    await axios.delete(`${apiPath}/NOAA/locationcategory`,{data:ids});
}
