public static class MotionInstance {

            public final Fixture fixture;
            public final Motion motion;
            public BukkitTask motionTask;
            public boolean run = true;
            public int curPos = 0;

            public MotionInstance(Fixture fixture, Motion motion) {
                this.fixture = fixture;
                this.motion = motion;
            }

            public Vector getNextVector() {
                Vector vector = motion.motions.get(curPos);
                curPos++;
                if (curPos >= motion.motions.size()) curPos = 0;
                return vector;
            }

            public void cancelTask() {
                if (motionTask != null)
                    motionTask.cancel();
                run = false;
            }

            public void startTask() {
                motionTask = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!run) return;

                        MotionPlayer.nextMotionStep(MotionInstance.this);
                        startTask();
                    }
                }.runTaskLaterAsynchronously(Main.instance, 0);
            }
        }