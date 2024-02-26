export interface NOAALocation{
    id:string,
    name:string,
    minDate:Date,
    maxDate:Date,
    dataCoverage:number,
    parent:string,
    loaded:boolean
}