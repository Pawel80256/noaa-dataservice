export interface PaginationWrapper<T> {
    offset:number,
    count:number,
    limit:number,
    data:T[]
}

export const initialPaginationWrapper:PaginationWrapper<any> = {
    offset:1,
    count:0,
    limit:0,
    data:[]
}