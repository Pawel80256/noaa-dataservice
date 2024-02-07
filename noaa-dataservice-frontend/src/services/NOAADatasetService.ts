import axios from 'axios';
import {NOAADataset} from "../models/NOAADataset";
import {initialPaginationWrapper, PaginationWrapper} from "../models/PaginationWrapper";

const apiPath = 'http://localhost:8080'

export const getAllDatasets = async (limit: number, offset: number): Promise<PaginationWrapper<NOAADataset>> => {
    try {
        const response = await axios.get<PaginationWrapper<NOAADataset>>(`${apiPath}/NOAA/datasets`, {
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