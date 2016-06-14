package ttl.larku.jconfig;

import java.util.regex.Pattern;

import org.springframework.core.type.filter.RegexPatternTypeFilter;

class ConfigFilter extends RegexPatternTypeFilter
{
    public ConfigFilter() {
        super(Pattern.compile("ttl\\.larku\\.jconfig\\..*"));
    }
}
