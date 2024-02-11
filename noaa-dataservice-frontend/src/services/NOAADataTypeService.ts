import axios from "axios";
import {NOAADataType} from "../models/NOAADataType";

const apiPath = 'http://localhost:8080'

export const getAllRemoteDataTypes = async (): Promise<NOAADataType[]> => {
    const response = await axios.get<NOAADataType[]>(`${apiPath}/NOAA/datatypes/remote`);
    return response.data;
};

export const getAllLocalDataTypes = async (): Promise<NOAADataType[]> => {
    const response = await axios.get<NOAADataType[]>(`${apiPath}/NOAA/datatypes`);
    return response.data;
};

export const deleteLocalDataTypesByIds = async(ids:string[]) => {
    await axios.delete(`${apiPath}/NOAA/datatypes`,{data:ids});
}

export const loadDataTypesByIds = async (ids:string[]) => {
    await axios.put(`${apiPath}/NOAA/datatypes/loadByIds`, ids)
}