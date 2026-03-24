# Smart Food Delivery Optimization System

Build a full-stack delivery optimization platform with a Java Spring Boot REST API backend and a React (Vite) frontend. The system models a city as a weighted directed graph and uses DSA algorithms (Dijkstra, Greedy Assignment, Priority Queue, 0/1 Knapsack) for delivery optimization.

## Proposed Changes

### Backend — Spring Boot (Maven)

#### [NEW] [pom.xml](file:///e:/dbms-sce/backend/pom.xml)
Maven project with Spring Boot 3.x, Java 17. Dependencies: `spring-boot-starter-web`, `lombok`.

---

#### Models (`com.fooddelivery.model`)

#### [NEW] [Node.java](file:///e:/dbms-sce/backend/src/main/java/com/fooddelivery/model/Node.java)
POJO: `int id, String name, double lat, double lng`

#### [NEW] [Edge.java](file:///e:/dbms-sce/backend/src/main/java/com/fooddelivery/model/Edge.java)
POJO: `int from, int to, int weight`

#### [NEW] [Order.java](file:///e:/dbms-sce/backend/src/main/java/com/fooddelivery/model/Order.java)
POJO: `String id, int restaurantNode, int customerNode, int priority, long timestamp, String status`

#### [NEW] [Agent.java](file:///e:/dbms-sce/backend/src/main/java/com/fooddelivery/model/Agent.java)
POJO: `String id, String name, int currentNode, AgentStatus status` (enum AVAILABLE/BUSY)

#### [NEW] [Graph.java](file:///e:/dbms-sce/backend/src/main/java/com/fooddelivery/model/Graph.java)
`Map<Integer, List<Edge>> adjacencyList` with helper methods.

---

#### DTOs (`com.fooddelivery.dto`)

#### [NEW] [OrderRequest.java](file:///e:/dbms-sce/backend/src/main/java/com/fooddelivery/dto/OrderRequest.java)
#### [NEW] [AgentRequest.java](file:///e:/dbms-sce/backend/src/main/java/com/fooddelivery/dto/AgentRequest.java)
#### [NEW] [RouteResponse.java](file:///e:/dbms-sce/backend/src/main/java/com/fooddelivery/dto/RouteResponse.java)
#### [NEW] [AssignmentResponse.java](file:///e:/dbms-sce/backend/src/main/java/com/fooddelivery/dto/AssignmentResponse.java)
#### [NEW] [ApiErrorResponse.java](file:///e:/dbms-sce/backend/src/main/java/com/fooddelivery/dto/ApiErrorResponse.java)

---

#### Algorithms (`com.fooddelivery.algorithm`)

#### [NEW] [DijkstraAlgorithm.java](file:///e:/dbms-sce/backend/src/main/java/com/fooddelivery/algorithm/DijkstraAlgorithm.java)
Shortest path using `PriorityQueue<int[]>` with `(distance, nodeId)`. Returns path + total distance.
- **Time**: O((V + E) log V) — **Space**: O(V)

#### [NEW] [GreedyAssigner.java](file:///e:/dbms-sce/backend/src/main/java/com/fooddelivery/algorithm/GreedyAssigner.java)
Iterates available agents, runs Dijkstra per agent to restaurant, picks minimum travel time.
- **Time**: O(A × (V + E) log V) — **Space**: O(V)

#### [NEW] [KnapsackDP.java](file:///e:/dbms-sce/backend/src/main/java/com/fooddelivery/algorithm/KnapsackDP.java)
0/1 Knapsack DP for batching orders (weight = delivery time, capacity = agent time).
- **Time**: O(n × W) — **Space**: O(n × W)

---

#### Repositories, Services, Controllers

In-memory repositories using `PriorityQueue<Order>` and `Map<String, Agent>`. Services wrap algorithm calls. Controllers expose REST endpoints:

| Endpoint | Method | Controller |
|---|---|---|
| `/api/orders` | POST, GET | OrderController |
| `/api/agents` | POST | AgentController |
| `/api/agents/available` | GET | AgentController |
| `/api/route?from=&to=` | GET | RouteController |
| `/api/assign` | POST | AssignmentController |
| `/api/graph` | GET | GraphController |

---

#### Config & Exception Handling

- **CorsConfig** — Allow React dev server
- **ApiException** + **GlobalExceptionHandler** — `@ControllerAdvice`
- **DataSeeder** — Seeds 10-node city graph with ~15 edges on startup

---

### Frontend — React (Vite)

| File | Purpose |
|---|---|
| `services/api.js` | Axios instance + all API methods |
| `hooks/useDelivery.js` | Custom hook for orders/agents/graph state |
| `components/GraphMap.jsx` | vis-network graph (red path highlights) |
| `components/OrderForm.jsx` | Place new order form |
| `components/AgentList.jsx` | Agent status badges |
| `components/RouteDisplay.jsx` | Shortest path + ETA display |
| `components/OrderQueue.jsx` | Live PQ table (3s auto-refresh) |
| `pages/Dashboard.jsx` | Main control panel |
| `pages/Simulation.jsx` | Live simulation view |
| `index.css` | Premium dark theme, glassmorphism, animations |

---

## Verification Plan

### Automated Tests
1. `cd e:\dbms-sce\backend && mvnw.cmd clean compile` — compilation succeeds
2. `cd e:\dbms-sce\backend && mvnw.cmd spring-boot:run` — starts on port 8080
3. `cd e:\dbms-sce\frontend && npm install && npm run build` — build succeeds
4. **API testing via browser**: GET `/api/graph`, POST `/api/orders`, GET `/api/route?from=1&to=5`, POST `/api/assign`

### Manual Verification
1. Start backend, then frontend dev server
2. Verify Dashboard renders with graph visualization
3. Place order → appears in queue; Register agent → appears in list
4. Compute route → path highlights on graph; Assign → agent assigned
