import {NOAADataDto} from "../models/NOAADataDto";
import axios from "axios";
const apiPath = 'http://localhost:8080'

export const getRemoteMeasurements = async(startDate:string | string[], endDate:string| string[], stationId: string) : Promise<NOAADataDto[]> => {
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

export const loadMeasurements = async(startDate: string | string [], endDate: string | string [], stationId: string) =>{
    await axios.put(`${apiPath}/NOAA/data/load`, null, {params :
            {
                datasetId:"GHCND",
                startDate:startDate,
                endDate:endDate,
                stationId: stationId
            }
    })
}