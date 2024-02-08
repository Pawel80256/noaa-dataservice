import axios from 'axios';
import {NOAADataset} from "../models/NOAADataset";
import {initialPaginationWrapper, PaginationWrapper} from "../models/PaginationWrapper";

const apiPath = 'http://localhost:8080'

export const getAllLocalDatasets = async () :Promise<NOAADataset[]> => {
    try{
        const response = await axios.get(`${apiPath}/NOAA/datasets`);
        return response.data;
    }catch (error){
        console.log(error);
        return [];
    }
}
export const getAllRemoteDatasets = async (limit: number, offset: number): Promise<PaginationWrapper<NOAADataset>> => {
    try {
        const response = await axios.get<PaginationWrapper<NOAADataset>>(`${apiPath}/NOAA/datasets/remote`, {
            params: {
                limit: limit,
                offset: offset
            }
        });
        return response.data;
    } catch (error) {
        console.error(error);
        return initialPaginationWrapper;
    }
};