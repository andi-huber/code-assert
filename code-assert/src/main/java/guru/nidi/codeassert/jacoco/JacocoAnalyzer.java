/*
 * Copyright (C) 2015 Stefan Niederhauser (nidin@gmx.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package guru.nidi.codeassert.jacoco;

import guru.nidi.codeassert.Analyzer;
import guru.nidi.codeassert.AnalyzerException;
import guru.nidi.codeassert.config.RejectCounter;
import guru.nidi.codeassert.config.ValuedLocation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class JacocoAnalyzer implements Analyzer<List<ValuedLocation>> {
    private final CoverageCollector collector;

    public JacocoAnalyzer(CoverageCollector collector) {
        this.collector = collector;
    }

    @Override
    public JacocoResult analyze() {
        final Coverages coverages = readReport();
        return filterResult(coverages);
    }

    private Coverages readReport() {
        final File coverage = new File("target/site/jacoco/jacoco.csv");
        if (!coverage.exists()) {
            throw new AnalyzerException("Coverage information in '" + coverage + "' does not exist.");
        }
        final Coverages coverages = new Coverages();
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(coverage), "utf-8"))) {
            String line;
            in.readLine();
            while ((line = in.readLine()) != null) {
                final String[] parts = line.split(",");
                coverages.add(new Coverage(parts[1], parts[2],
                        Integer.parseInt(parts[3]), Integer.parseInt(parts[4]),
                        Integer.parseInt(parts[5]), Integer.parseInt(parts[6]),
                        Integer.parseInt(parts[7]), Integer.parseInt(parts[8]),
                        Integer.parseInt(parts[9]), Integer.parseInt(parts[10]),
                        Integer.parseInt(parts[11]), Integer.parseInt(parts[12])));
            }
        } catch (IOException e) {
            throw new AnalyzerException("Problem analyzing coverage", e);
        }
        return coverages;
    }

    private JacocoResult filterResult(Coverages coverages) {
        final List<ValuedLocation> filtered = new ArrayList<>();
        final RejectCounter counter = new RejectCounter();
        final ValuedLocation global = coverages.global.toValuedLocation(collector.types);
        if (collector.accept(counter.issue(global))) {
            filtered.add(global);
        }
        for (final Coverage c : coverages.perPackage.values()) {
            final ValuedLocation vc = c.toValuedLocation(collector.types);
            if (collector.accept(counter.issue(vc))) {
                filtered.add(vc);
            }
        }
        for (final Coverage c : coverages.coverages) {
            final ValuedLocation vc = c.toValuedLocation(collector.types);
            if (collector.accept(counter.issue(vc))) {
                filtered.add(vc);
            }
        }
        collector.printUnusedWarning(counter);
        return new JacocoResult(this, filtered, collector.unusedActions(counter), collector.types);
    }
}
