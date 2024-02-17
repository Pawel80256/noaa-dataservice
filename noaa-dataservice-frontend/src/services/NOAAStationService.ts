import {NOAALocation} from "../models/NOAALocation";
import axios from "axios";
import {NOAAStation} from "../models/NOAAStation";
const apiPath = 'http://localhost:8080'

export const getRemoteStationsByLocationId = async (locationId:string): Promise<NOAAStation[]> =>{
    const response = await axios.get<NOAAStation[]>(`${apiPath}/NOAA/station/location/` + locationId)
    return response.data;
}