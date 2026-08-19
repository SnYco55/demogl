export interface Member {
  id: number
  firstname: string
  lastname: string
  start: string
  end: string | null
  createdAt: string
}

export interface MemberDetails extends Member {
  services: Service[]
  roles: Role[]
}

export interface Role {
  id: number
  name: string
  createdAt: string
}

export interface Department {
  id: string
  facultyId: string
  createdAt: string
}

export interface DepartmentCreateRequest {
  id: string
  facultyId: string
}

export interface DepartmentPatchRequest {
  facultyId: string
}

export interface Faculty {
  id: string
  name: string
  createdAt: string
}

export interface FacultyCreateRequest {
  id: string
  name: string
}

export interface FacultyPatchRequest {
  name: string
}

export interface Service {
  id: string
  name: string
  department: Department
  faculty: Faculty
  director: Member
  createdAt: string
}

export interface ServiceListItem {
  id: string
  name: string
  departmentId: string
  directorId: number
  createdAt: string
}

export interface ServiceCreateRequest {
  id: string
  name: string
  departmentId: string
  directorId: number
}

export interface ServicePatchRequest {
  name?: string
  departmentId?: string
  directorId?: number
}
