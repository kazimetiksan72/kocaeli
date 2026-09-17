export type User={id:string;username:string;fullName:string;unitId:string;roles:string[]};
export type CityRequest={id:string;requestNo:string;title:string;description:string;requestType:string;channel:string;priority:string;applicantName?:string;responsibleUnitId:string;assigneeId?:string;districtId:string;neighborhoodId:string;address?:string;geometryWkt:string;externalReference?:string;status:string;createdAt:string;updatedAt:string;version:number};
export type Page<T>={content:T[];page:number;size:number;totalElements:number;totalPages:number};

