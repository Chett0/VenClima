package com.example.venclima.models;

import java.util.List;

public class IslandsTide {
        private List<Island> islands;
        private List<Tide> tides;

        public List<Island> getIslands() {
            return islands;
        }

        public void setIslands(List<Island> islands) {
            this.islands = islands;
        }

        public List<Tide> getTides() {
            return tides;
        }

        public void setTides(List<Tide> tides) {
            this.tides = tides;
        }
}
