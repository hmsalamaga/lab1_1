/*
 * Copyright 2011-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package pl.com.bottega.ecommerce.sales.domain.offer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class OfferItem {
    
    private Product product;

    private int quantity;

    private Money totalCost;

    // discount
    private String discountCause;

    private BigDecimal discount;

    public OfferItem(Product product, int quantity, String currency) {
        this(product, quantity, null, null, currency);
    }

    public OfferItem(Product product, int quantity, BigDecimal discount, String discountCause, String currency) {
        this.product = product;
        this.quantity = quantity;
        this.discount = discount;
        this.discountCause = discountCause;

        BigDecimal discountValue = new BigDecimal(0);
        if (discount != null) {
            discountValue = discountValue.add(discount);
        }

        BigDecimal totalCost = product.getPriceValue().multiply(new BigDecimal(quantity)).subtract(discountValue);

        this.totalCost = new Money(totalCost, currency);
    }

    public String getProductId() {
        return product.getId();
    }

    public BigDecimal getProductPrice() {
        return product.getPriceValue();
    }

    public String getProductName() {
        return product.getName();
    }

    public Date getProductSnapshotDate() {
        return product.getSnapshotDate();
    }

    public String getProductType() {
        return product.getType();
    }

    public BigDecimal getTotalCost() {
        return totalCost.getValue();
    }

    public String getTotalCostCurrency() {
        return totalCost.getCurrency();
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public String getDiscountCause() {
        return discountCause;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTotalCostCurrency(), discount, discountCause, getProductId(), getProductName(), getProductPrice(),
                getProductSnapshotDate(), getProductType(), quantity, getTotalCost());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        OfferItem other = (OfferItem) obj;
        return Objects.equals(getTotalCostCurrency(), other.getTotalCostCurrency())
               && Objects.equals(discount, other.discount)
               && Objects.equals(discountCause, other.discountCause)
               && Objects.equals(getProductId(), other.getProductId())
               && Objects.equals(getProductName(), other.getProductName())
               && Objects.equals(getProductPrice(), other.getProductPrice())
               && Objects.equals(getProductSnapshotDate(), other.getProductSnapshotDate())
               && Objects.equals(getProductType(), other.getProductType())
               && quantity == other.quantity
               && Objects.equals(totalCost.getValue(), other.totalCost.getValue());
    }

    /**
     * @param other
     * @param delta acceptable percentage difference
     * @return
     */
    public boolean sameAs(OfferItem other, double delta) {
        if (getProductPrice() == null) {
            if (other.getProductPrice() != null) {
                return false;
            }
        } else if (!getProductPrice().equals(other.getProductPrice())) {
            return false;
        }
        if (getProductName() == null) {
            if (other.getProductName() != null) {
                return false;
            }
        } else if (!getProductName().equals(other.getProductName())) {
            return false;
        }

        if (getProductId() == null) {
            if (other.getProductId() != null) {
                return false;
            }
        } else if (!getProductId().equals(other.getProductId())) {
            return false;
        }
        if (getProductType() == null) {
            if (other.getProductType() != null) {
                return false;
            }
        } else if (!getProductType().equals(other.getProductType())) {
            return false;
        }

        if (quantity != other.quantity) {
            return false;
        }

        BigDecimal max;
        BigDecimal min;
        if (totalCost.getValue().compareTo(other.totalCost.getValue()) > 0) {
            max = totalCost.getValue();
            min = other.totalCost.getValue();
        } else {
            max = other.totalCost.getValue();
            min = totalCost.getValue();
        }

        BigDecimal difference = max.subtract(min);
        BigDecimal acceptableDelta = max.multiply(BigDecimal.valueOf(delta / 100));

        return acceptableDelta.compareTo(difference) > 0;
    }

}
