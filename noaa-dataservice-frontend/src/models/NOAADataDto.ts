export interface NOAAData {
    id:string,//custom
    datatype:string,
    station:string,
    date:Date,
    attributes:string,
    value: number,
    loaded: boolean
}