import {NOAALocation} from "../models/NOAALocation";
import axios from "axios";
import {NOAAStation} from "../models/NOAAStation";
const apiPath = 'http://localhost:8080'

export const getRemoteStationsByLocationId = async (locationId:string): Promise<NOAAStation[]> =>{
    const response = await axios.get<NOAAStation[]>(`${apiPath}/NOAA/station/location/` + locationId)
    return response.data;
}

export const getAllLocalStations = async (): Promise<NOAAStation[]> =>{
    const response = await axios.get<NOAAStation[]>(`${apiPath}/NOAA/station`);
    return response.data;
}

export const loadByIdsAndLocationId = async (locationId:string, stationIds:string[]) => {
    await axios.put(`${apiPath}/NOAA/station/load/` + locationId, stationIds)
}

export const deleteStationsByIds = async (stationsIds: string[]) => {
    await axios.delete(`${apiPath}/NOAA/station`, {data:stationsIds});
}