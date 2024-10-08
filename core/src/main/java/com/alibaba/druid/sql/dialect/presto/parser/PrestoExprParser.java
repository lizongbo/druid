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
package com.alibaba.druid.sql.dialect.presto.parser;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.dialect.presto.ast.PrestoColumnWith;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.Token;

/**
 * Created by wenshao on 16/9/13.
 */
public class PrestoExprParser extends SQLExprParser {
    public PrestoExprParser(String sql, SQLParserFeature... features) {
        this(new PrestoLexer(sql, features));
        this.lexer.nextToken();
    }

    public PrestoExprParser(Lexer lexer) {
        super(lexer);
        dbType = DbType.presto;
    }

    @Override
    protected SQLColumnDefinition parseColumnSpecific(SQLColumnDefinition column) {
        if (lexer.token() == Token.WITH) {
            lexer.nextToken();
            PrestoColumnWith prestoColumnWith = new PrestoColumnWith();
            accept(Token.LPAREN);
            parseAssignItems(prestoColumnWith.getProperties(), prestoColumnWith, false);
            accept(Token.RPAREN);
            column.addConstraint(prestoColumnWith);
            return parseColumnRest(column);
        }
        return column;
    }
}
