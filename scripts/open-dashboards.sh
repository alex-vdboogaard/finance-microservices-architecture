#!/usr/bin/env bash
# ==============================================================================
# Open Dashboards & Visualizations Script
# Launches all localhost service web UIs and monitoring dashboards in your browser.
# ==============================================================================

set -euo pipefail

# Color palette for terminal output
BOLD="\033[1m"
GREEN="\033[32m"
BLUE="\033[34m"
YELLOW="\033[33m"
CYAN="\033[36m"
RED="\033[31m"
RESET="\033[0m"

# Define Dashboard Services: Name | URL | Description
DASHBOARDS=(
    "Frontend UI|http://localhost:5173|React/Vite Finance Web Application"
    "Eureka Registry|http://localhost:8761|Netflix Eureka Microservices Registry"
    "API Gateway|http://localhost:8080|Spring Cloud API Gateway"
    "Kafka UI|http://localhost:8081|Kafka Topics & Broker Monitoring UI"
    "Grafana|http://localhost:3000|Metrics & Log Dashboards (admin/password)"
    "Prometheus|http://localhost:9090|Prometheus Metrics & Target Status"
    "Zipkin Tracing|http://localhost:9411|Distributed Tracing Visualization"
)

# Optional Microservice Actuator endpoints
ACTUATOR_ENDPOINTS=(
    "Account Service Health|http://localhost:8005/actuator/health|Account Service Actuator"
    "Audit Log Service Health|http://localhost:8001/actuator/health|Audit Log Service Actuator"
    "Transaction Service Health|http://localhost:8003/actuator/health|Transaction Service Actuator"
    "Notification Service Health|http://localhost:8007/actuator/health|Notification Service Actuator"
)

# Function to detect browser open command according to OS
get_open_command() {
    if command -v open >/dev/null 2>&1; then
        echo "open" # macOS
    elif command -v xdg-open >/dev/null 2>&1; then
        echo "xdg-open" # Linux
    elif command -v powershell.exe >/dev/null 2>&1; then
        echo "powershell.exe Start-Process" # WSL / Windows
    elif command -v cmd.exe >/dev/null 2>&1; then
        echo "cmd.exe /c start" # Windows CMD
    else
        echo ""
    fi
}

show_help() {
    echo -e "${BOLD}Usage:${RESET} $0 [OPTIONS]"
    echo ""
    echo -e "${BOLD}Options:${RESET}"
    echo -e "  ${GREEN}--list${RESET}        Display all dashboard URLs without opening browser"
    echo -e "  ${GREEN}--check${RESET}       Check HTTP accessibility of endpoints before opening"
    echo -e "  ${GREEN}--actuators${RESET}   Include Spring Boot Actuator endpoints in browser"
    echo -e "  ${GREEN}--help, -h${RESET}    Show this help message"
    echo ""
}

list_dashboards() {
    local include_actuators="${1:-false}"
    echo -e "${BOLD}${BLUE}=== Microservice Architecture Visualizations & Dashboards ===${RESET}"
    echo ""
    printf "${BOLD}%-25s %-32s %-40s${RESET}\n" "SERVICE" "URL" "DESCRIPTION"
    printf "%-25s %-32s %-40s\n" "-------------------------" "--------------------------------" "----------------------------------------"
    
    for item in "${DASHBOARDS[@]}"; do
        IFS="|" read -r name url desc <<< "$item"
        printf "${CYAN}%-25s${RESET} ${GREEN}%-32s${RESET} %-40s\n" "$name" "$url" "$desc"
    done

    if [[ "$include_actuators" == "true" ]]; then
        echo ""
        echo -e "${BOLD}${YELLOW}--- Actuator Endpoints ---${RESET}"
        for item in "${ACTUATOR_ENDPOINTS[@]}"; do
            IFS="|" read -r name url desc <<< "$item"
            printf "${CYAN}%-25s${RESET} ${GREEN}%-32s${RESET} %-40s\n" "$name" "$url" "$desc"
        done
    fi
    echo ""
}

check_endpoint() {
    local url="$1"
    if command -v curl >/dev/null 2>&1; then
        if curl --output /dev/null --silent --head --fail --connect-timeout 2 "$url"; then
            return 0
        else
            return 1
        fi
    fi
    return 0
}

open_dashboards() {
    local check_first="${1:-false}"
    local include_actuators="${2:-false}"
    local open_cmd
    open_cmd=$(get_open_command)

    if [[ -z "$open_cmd" ]]; then
        echo -e "${RED}Error: No suitable browser launcher command found (open/xdg-open).${RESET}"
        echo "Listing URLs instead:"
        list_dashboards "$include_actuators"
        exit 1
    fi

    echo -e "${BOLD}${GREEN}Opening visualization dashboards in your browser...${RESET}"
    echo ""

    local items_to_open=("${DASHBOARDS[@]}")
    if [[ "$include_actuators" == "true" ]]; then
        items_to_open+=("${ACTUATOR_ENDPOINTS[@]}")
    fi

    for item in "${items_to_open[@]}"; do
        IFS="|" read -r name url desc <<< "$item"
        if [[ "$check_first" == "true" ]]; then
            echo -n -e "Checking ${CYAN}$name${RESET} ($url)... "
            if check_endpoint "$url"; then
                echo -e "${GREEN}UP${RESET} - Opening tab"
                $open_cmd "$url" >/dev/null 2>&1 || true
            else
                echo -e "${RED}DOWN / UNREACHABLE${RESET} - Skipping"
            fi
        else
            echo -e "Opening ${CYAN}$name${RESET} -> ${GREEN}$url${RESET}"
            $open_cmd "$url" >/dev/null 2>&1 || true
        fi
        sleep 0.2
    done

    echo ""
    echo -e "${BOLD}${GREEN}All requested dashboards opened successfully!${RESET}"
}

main() {
    local list_only=false
    local check_first=false
    local include_actuators=false

    while [[ $# -gt 0 ]]; do
        case "$1" in
            --list)
                list_only=true
                shift
                ;;
            --check)
                check_first=true
                shift
                ;;
            --actuators)
                include_actuators=true
                shift
                ;;
            --help|-h)
                show_help
                exit 0
                ;;
            *)
                echo -e "${RED}Unknown option:${RESET} $1"
                show_help
                exit 1
                ;;
        esac
    done

    if [[ "$list_only" == "true" ]]; then
        list_dashboards "$include_actuators"
    else
        open_dashboards "$check_first" "$include_actuators"
    fi
}

main "$@"
