package com.carbonfive.sstemplates;

import com.carbonfive.sstemplates.hssf.HssfCellAccumulator;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import java.util.HashMap;
import java.util.Map;

public class SsTemplateVariableMapper extends VariableMapper {
    private final Map<String, Object> map = new HashMap<>();
    private final Map<String, ValueExpression> mapValueExpression = new HashMap<>();
    private final Map<String, HssfCellAccumulator> accumulatorCache = new HashMap<>();
    private final ExpressionFactory expressionFactory;

    public SsTemplateVariableMapper(final ExpressionFactory expressionFactory) {
        this.expressionFactory = expressionFactory;
        setDefaultVariables();
    }

    public ValueExpression resolveVariable(String variable) {
        if ("pageScope".equals(variable)) {
            return getExpressionFactory().createValueExpression(map, map.getClass());
        }

        if ("accumulator".equals(variable)) {
            return getExpressionFactory().createValueExpression(accumulatorCache, accumulatorCache.getClass());
        }

        return mapValueExpression.get(variable);
    }

    public void removeVariable(String variable) {
        this.map.remove(variable);
        this.mapValueExpression.remove(variable);
    }

    public ValueExpression setVariable(String variable, ValueExpression expression) {
        this.map.put(variable, expression.getValue(null));
        return this.mapValueExpression.put(variable, expression);
    }

    public void setVariable(String variable, Object value) {
        if (value == null) {
            this.mapValueExpression.put(variable, expressionFactory.createValueExpression(null, Object.class));
        } else {
            this.mapValueExpression.put(variable, expressionFactory.createValueExpression(value, value.getClass()));
        }
        this.map.put(variable, value);
    }

    public HssfCellAccumulator getAccumulator(final String name) {
        HssfCellAccumulator acc = accumulatorCache.get(name);
        if (acc == null) {
            acc = new HssfCellAccumulator();
            accumulatorCache.put(name, acc);
        }
        return acc;
    }

    public ExpressionFactory getExpressionFactory() {
        return this.expressionFactory;
    }

    private void setDefaultVariables() {
        setVariable("param", new HashMap<String, Object>());
        setVariable("paramValues", new HashMap<String, Object>());
    }
}
