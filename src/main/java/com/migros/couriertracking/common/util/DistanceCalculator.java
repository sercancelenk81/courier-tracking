package com.migros.couriertracking.common.util;

public interface DistanceCalculator {

    Double calculate(double lat1, double lng1, double lat2, double lng2);

}
