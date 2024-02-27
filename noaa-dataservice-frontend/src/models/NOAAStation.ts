export interface NOAAStation{
    id:string,
    name:string,
    elevation:number,
    minDate:Date,
    maxDate:Date,
    latitude:number,
    longitude:number,
    dataCoverage:number,
    elevationUnit:string,
    locationId:string,
    loaded:boolean
}