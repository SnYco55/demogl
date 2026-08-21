import type {
  FacultyCreateRequest,
  DepartmentCreateRequest,
  Role,
  ServiceCreateRequest,
  Member,
} from '../types/type'

const API = 'http://localhost:8080'

async function post<T>(url: string, body: object): Promise<T> {
  const response = await fetch(`${API}${url}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(body),
  })

  if (!response.ok) {
    throw new Error(`POST ${url} → ${response.status}: ${await response.text()}`)
  }

  return response.json()
}

async function patch(url: string, body: object) {
  const response = await fetch(`${API}${url}`, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(body),
  })

  if (!response.ok) {
    throw new Error(`PATCH ${url} → ${response.status}: ${await response.text()}`)
  }
}

async function seed() {
  await post<FacultyCreateRequest>('/faculties', {
    id: 'fs',
    name: 'Falculté des Sciences',
  })

  await post<FacultyCreateRequest>('/faculties', {
    id: 'fpms',
    name: 'Faculté Polytechnique de Mons',
  })

  await post<DepartmentCreateRequest>('/departments', {
    id: 'informatique',
    facultyId: 'fs',
  })

  await post<DepartmentCreateRequest>('/departments', {
    id: 'informatique-de-gestion',
    facultyId: 'fpms',
  })

  const professeur = await post<Role>('/roles', {
    name: 'Professeur',
  })

  const chercheur = await post<Role>('/roles', {
    name: 'Chercheur',
  })

  const assistant = await post<Role>('/roles', {
    name: 'Assistant',
  })

  const tom = await post<Member>('/members', {
    firstname: 'Tom',
    lastname: 'Mens',
    start: '2026-08-14T12:41:00',
    end: null,
    serviceIds: [],
    roleIds: [professeur.id, chercheur.id],
  })

  const stephane = await post<Member>('/members', {
    firstname: 'Stéphane',
    lastname: 'Dupont',
    start: '2026-08-04T12:53:00',
    end: null,
    serviceIds: [],
    roleIds: [professeur.id, chercheur.id],
  })

  const mohammed = await post<Member>('/members', {
    firstname: 'Mohammed',
    lastname: 'Benjelloun',
    start: '2026-08-14T13:05:00',
    end: null,
    serviceIds: [],
    roleIds: [professeur.id],
  })

  await post<ServiceCreateRequest>('/services', {
    id: 'S852',
    name: 'Service de Génie Logiciel',
    departmentId: 'informatique',
    directorId: tom.id,
  })

  await post<ServiceCreateRequest>('/services', {
    id: 'S841',
    name: "Service d'Intelligence Artificielle",
    departmentId: 'informatique',
    directorId: stephane.id,
  })

  await post<ServiceCreateRequest>('/services', {
    id: 'F114',
    name: 'Service Informatique, Logiciel et Intelligence artificielle',
    departmentId: 'informatique-de-gestion',
    directorId: mohammed.id,
  })

  await patch(`/members/${tom.id}`, {
    serviceIds: ['S852'],
  })

  await patch(`/members/${stephane.id}`, {
    serviceIds: ['S841', 'F114'],
  })

  await patch(`/members/${mohammed.id}`, {
    serviceIds: ['F114'],
  })

  console.log('Seed terminé avec succès.')
}

seed().catch((error) => {
  console.error('Erreur pendant le seed :', error)
})
