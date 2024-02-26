export interface NOAALocation{
    id:string,
    name:string,
    minDate:Date,
    maxDate:Date,
    dataCoverage:number,
    parentId:string,
    loaded:boolean
}