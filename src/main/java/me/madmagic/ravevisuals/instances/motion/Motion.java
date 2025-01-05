public static class Motion {

        public final String name;
        public final int delay;
        public final boolean relative;
        public final List<Vector> motions;

        public Motion(ConfigurationSection config, String name) {
            this.name = name;
            delay = config.getInt("delay");
            relative = config.getBoolean("relative");

            motions = new ArrayList<>();
            config.getStringList("motion").forEach(s -> {
                double[] split = Stream.of(s.split(" ")).mapToDouble(Double::parseDouble).toArray();
                motions.add(new Vector(PositioningHelper.fixRotation((float) split[0]), 0, PositioningHelper.fixRotation((float) split[1])));
            });
        }
    }