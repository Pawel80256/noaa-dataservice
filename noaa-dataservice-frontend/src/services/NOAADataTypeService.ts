import {initialPaginationWrapper, PaginationWrapper} from "../models/PaginationWrapper";
import {NOAADataset} from "../models/NOAADataset";
import axios from "axios";
import {NOAADataType} from "../models/NOAADataType";
const apiPath = 'http://localhost:8080'

export const getAllRemoteDataTypes = async (): Promise<NOAADataType[]> => {
    const response = await axios.get<NOAADataType[]>(`${apiPath}/NOAA/datatypes/remote`);
    return response.data;
};