export interface NOAAStation{
    id:string,
    name:string,
    elevation:number,
    mindate:Date,
    maxdate:Date,
    latitude:number,
    longitude:number,
    datacoverage:number,
    elevationUnit:string
}