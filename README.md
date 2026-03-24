# Smart Food Delivery Optimization System

[![Java Version](https://img.shields.io/badge/Java-17-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-Vite-blue.svg)](https://reactjs.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](#)
[![Version](https://img.shields.io/badge/Version-1.0.0-lightgrey.svg)](#)

## What the Project Does

The Smart Food Delivery Optimization System is a full-stack platform designed to streamline and visualize food delivery logistics. It models a city's road network as a weighted directed graph and leverages advanced Data Structures and Algorithms (DSA) to intelligently manage order processing, finding the fastest routes, and assigning delivery agents efficiently.

The system features a robust Spring Boot backend for computational optimization and a responsive React frontend for real-time simulation and visualization.

## Why the Project is Useful

This system significantly improves delivery logistics by reducing delivery times and optimizing agent workloads. Key features include:

*   **Graph-Based Route Optimization:** Utilizes Dijkstra's algorithm to compute the shortest and most efficient delivery paths across the city.
*   **Intelligent Agent Assignment:** Employs a greedy algorithm to evaluate all available agents and assign the optimal candidate based on minimum travel time to the restaurant.
*   **Priority-Based Scheduling:** Manages incoming orders using a priority queue, ensuring that high-value or urgent deliveries are processed first.
*   **Order Batching:** Uses 0/1 Knapsack dynamic programming to optimize batch assignments, balancing delivery times with agent capacities.
*   **Real-Time Visualization:** Provides a visual dashboard to monitor the live state of the delivery network, order queues, and agent statuses.

## How Users Can Get Started

### Prerequisites

Ensure you have the following installed on your machine:
*   Java Development Kit (JDK) 17 or higher
*   Node.js 18 or higher
*   Maven

### Installation and Setup

#### 1. Backend Setup

Navigate to the `backend` directory, clean the project, and run the Spring Boot application using the Maven wrapper.

```bash
cd backend
./mvnw clean compile
./mvnw spring-boot:run
```

The backend REST API will start and listen on `http://localhost:8080`.

#### 2. Frontend Setup

Open a new terminal, navigate to the `frontend` directory, install the required NPM packages, and start the development server.

```bash
cd frontend
npm install
npm run dev
```

The React frontend will be accessible in your browser at `http://localhost:5173`.

### Usage Examples

You can interact directly with the backend REST APIs using tools like `curl` or Postman.

**Retrieve the City Graph Network:**
```bash
curl -X GET http://localhost:8080/api/graph
```

**Calculate the Shortest Route:**
```bash
curl -X GET "http://localhost:8080/api/route?from=1&to=5"
```

**Retrieve Available Agents:**
```bash
curl -X GET http://localhost:8080/api/agents/available
```

## Where Users Can Get Help

If you encounter any issues, require support, or have questions about the system architecture, please use the following resources:

*   **Issue Tracker:** Report bugs or request new features by opening an issue on the repository's Issue Tracker.
*   **Documentation:** Review the `docs/` directory for detailed API specifications and architectural diagrams (if available).

## Who Maintains and Contributes

This project is actively maintained by the core development team. We strongly encourage community contributions, bug fixes, and feature enhancements.

### Contribution Guidelines

If you wish to contribute to the project, please follow these steps:

1.  Fork the repository.
2.  Create a new feature branch: `git checkout -b feature/your-feature-name`
3.  Commit your changes: `git commit -m 'Add detailed description of your feature'`
4.  Push to the branch: `git push origin feature/your-feature-name`
5.  Open a Pull Request against the main branch.

For more detailed information on coding standards and the PR process, please refer to the `docs/CONTRIBUTING.md` file.
