package com.shopproject.shopbt.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataTodayResponse {
    BigDecimal total_income;
    Integer quantity_person_register;
    Integer quantity_orders;
    List<Integer> quantity_users_in_chart;
    List<Integer> quantity_orders_in_chart;
}

