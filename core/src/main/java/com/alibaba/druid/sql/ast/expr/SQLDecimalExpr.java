/*
 * Copyright 1999-2017 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class SQLDecimalExpr extends SQLNumericLiteralExpr implements SQLValuableExpr, Comparable<SQLDecimalExpr> {
    public SQLDecimalExpr() {
        super(new SQLDataTypeImpl(SQLDataType.Constants.DECIMAL));
    }

    public SQLDecimalExpr(BigDecimal value) {
        this();
        this.value = value;
    }

    public SQLDecimalExpr(String value) {
        this();
        this.value = new BigDecimal(value);
    }

    public SQLDecimalExpr clone() {
        return new SQLDecimalExpr(getValue());
    }

    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public Number getNumber() {
        return getValue();
    }

    public BigDecimal getValue() {
        return (BigDecimal) value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);

        visitor.endVisit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
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
        SQLDecimalExpr other = (SQLDecimalExpr) obj;
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public void setNumber(Number number) {
        if (number == null) {
            this.value = null;
            return;
        }

        this.setValue(((BigDecimal) number));
    }

    @Override
    public int compareTo(SQLDecimalExpr o) {
        return getValue().compareTo(o.getValue());
    }

    public SQLDecimalExpr negative() {
        return new SQLDecimalExpr(getValue().negate());
    }
}
