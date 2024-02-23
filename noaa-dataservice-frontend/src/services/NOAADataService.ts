import {NOAAData} from "../models/NOAAData";
import axios from "axios";
const apiPath = 'http://localhost:8080'

export const getRemoteMeasurements = async(startDate:string | string[], endDate:string| string[], stationId: string) : Promise<NOAAData[]> => {
    const response = await axios.get(`${apiPath}/NOAA/data/remote`,{params:
            {
                datasetId:"GHCND",
                startDate:startDate,
                endDate:endDate,
                stationId: stationId
            }
    })
    return response.data;
}