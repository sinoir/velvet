package com.delectable.mobile.api.models;

public class Boundaries {

    private Boundary from;

    private Boundary to;

    public Boundaries(Boundary from, Boundary to) {
        this.from = from;
        this.to = to;
    }

    public Boundary getFrom() {
        return from;
    }

    public Boundary getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "Boundaries{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }

    public static class Boundary {

        private String before;

        private String after;

        private String since;

        public Boundary(String before, String after, String since) {
            this.before = before;
            this.after = after;
            this.since = since;
        }

        public String getSince() {
            return since;
        }

        public String getAfter() {
            return after;
        }

        public String getBefore() {
            return before;
        }

        @Override
        public String toString() {
            return "Boundary{" +
                    "before='" + before + '\'' +
                    ", after='" + after + '\'' +
                    ", since=" + since +
                    '}';
        }
    }
}
