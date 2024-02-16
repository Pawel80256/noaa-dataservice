import axios from 'axios';
import {NOAADataset} from "../models/NOAADataset";
import {initialPaginationWrapper, PaginationWrapper} from "../models/PaginationWrapper";

const apiPath = 'http://localhost:8080'

export const getAllLocalDatasets = async () :Promise<NOAADataset[]> => {
        const response = await axios.get(`${apiPath}/NOAA/datasets`);
        return response.data;
}

export const getAllRemoteDatasets = async (): Promise<NOAADataset[]> => {
        const response = await axios.get<NOAADataset[]>(`${apiPath}/NOAA/datasets/remote`);
        return response.data;
};

export const loadByIds = async (ids:string[], singly:boolean) => {
        await axios.put(`${apiPath}/NOAA/datasets/loadByIds`,ids, {params: {singly:singly}});
}

export const deleteDatasetsByIds = async(ids:string[]) =>{
        await axios.delete(`${apiPath}/NOAA/datasets`,{data:ids});
}