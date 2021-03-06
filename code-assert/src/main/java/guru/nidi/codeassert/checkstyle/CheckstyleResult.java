/*
 * Copyright © 2015 Stefan Niederhauser (nidin@gmx.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package guru.nidi.codeassert.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import guru.nidi.codeassert.Analyzer;
import guru.nidi.codeassert.AnalyzerResult;

import java.util.List;

public class CheckstyleResult extends AnalyzerResult<List<AuditEvent>> {
    CheckstyleResult(Analyzer<List<AuditEvent>> analyzer,
                     List<AuditEvent> findings, List<String> unusedActions) {
        super(analyzer, findings, unusedActions);
    }
}
