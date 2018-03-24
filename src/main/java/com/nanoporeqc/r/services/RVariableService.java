package com.nanoporeqc.r.services;

import com.nanoporeqc.r.domain.RVariable;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RVariableService {

    public List getDataSetFromR(RVariable rVariable, RConnection connection) {
        try {
            switch (rVariable.getType()) {
                case LOGICAL:
                    return Arrays.stream(connection.eval(rVariable.getName()).asIntegers()).boxed().collect(Collectors.toList());
                case DOUBLE:
                    return Arrays.stream(connection.eval(rVariable.getName()).asDoubles()).boxed().collect(Collectors.toList());
                case NUMERIC:
                    return Arrays.stream(connection.eval(rVariable.getName()).asIntegers()).boxed().collect(Collectors.toList());
                case CHARACTER:
                    return Arrays.asList(connection.eval(rVariable.getName()).asStrings());
                default:
                    break;
            }
        } catch (REXPMismatchException | RserveException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
