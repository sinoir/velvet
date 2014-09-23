
package com.delectable.mobile.api.models;

import com.google.gson.annotations.SerializedName;

public class Motd {

    private int type;

    private String url;

    @SerializedName("client-config")
    private ClientConfig clientConfig;

    public int getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public ClientConfig getClientConfig() {
        return clientConfig;
    }

    public static class ClientConfig {

        private MonocleMode monocle_mode;

        private Sharing sharing;

        private ServerSettings server_settings;

        private Appirater appirater;

        private SmsTest sms_test;

        private CaptureSettings capture_settings;

        public MonocleMode getMonocleMode() {
            return monocle_mode;
        }

        public boolean isMonocleModeEnabled() {
            return monocle_mode.isEnabled();
        }

        public Sharing getSharing() {
            return sharing;
        }

        public ServerSettings getServerSettings() {
            return server_settings;
        }

        public Appirater getAppirater() {
            return appirater;
        }

        public SmsTest getSmsTest() {
            return sms_test;
        }

        public CaptureSettings getCaptureSettings() {
            return capture_settings;
        }

        public String getCaptureString() {
            return capture_settings.getCaptureString();
        }

        //region Inner Classes

        public static class MonocleMode {

            private boolean enabled;

            public boolean isEnabled() {
                return enabled;
            }

            @Override
            public String toString() {
                return "MonocleMode{" +
                        "enabled=" + enabled +
                        '}';
            }
        }

        public static class Sharing {

            private String fb_share_caption;

            private String fb_share_name;

            private String fb_share_description;

            public String getFbShareCaption() {
                return fb_share_caption;
            }

            public String getFbShareName() {
                return fb_share_name;
            }

            public String getFbShareDescription() {
                return fb_share_description;
            }

            @Override
            public String toString() {
                return "Sharing{" +
                        "fb_share_caption='" + fb_share_caption + '\'' +
                        ", fb_share_name='" + fb_share_name + '\'' +
                        ", fb_share_description='" + fb_share_description + '\'' +
                        '}';
            }
        }

        public static class ServerSettings {

            private int contact_list_batch_size;

            private int max_batch_size;

            private int retry_batch_size;

            private int max_retries;

            public int getContactListBatchSize() {
                return contact_list_batch_size;
            }

            public int getMaxBatchSize() {
                return max_batch_size;
            }

            public int getRetryBatchSize() {
                return retry_batch_size;
            }

            public int getMaxRetries() {
                return max_retries;
            }

            @Override
            public String toString() {
                return "ServerSettings{" +
                        "contact_list_batch_size=" + contact_list_batch_size +
                        ", max_batch_size=" + max_batch_size +
                        ", retry_batch_size=" + retry_batch_size +
                        ", max_retries=" + max_retries +
                        '}';
            }
        }

        public static class Appirater {

            private int significant_events_until_rate_app_prompt;

            private int uses_until_rate_app_prompt;

            private int days_until_rate_app_prompt;

            private int days_until_reminding_to_rate;

            public int getSignificantEventsUntilRateAppPrompt() {
                return significant_events_until_rate_app_prompt;
            }

            public int getUsesUntilRateAppPrompt() {
                return uses_until_rate_app_prompt;
            }

            public int getDaysUntilRateAppPrompt() {
                return days_until_rate_app_prompt;
            }

            public int getDaysUntilRemindingToRate() {
                return days_until_reminding_to_rate;
            }

            @Override
            public String toString() {
                return "Appirater{" +
                        "significant_events_until_rate_app_prompt="
                        + significant_events_until_rate_app_prompt +
                        ", uses_until_rate_app_prompt=" + uses_until_rate_app_prompt +
                        ", days_until_rate_app_prompt=" + days_until_rate_app_prompt +
                        ", days_until_reminding_to_rate=" + days_until_reminding_to_rate +
                        '}';
            }
        }

        public static class SmsTest {

            private String sms_body;

            private int max_number_of_group_messages_to_send;

            private int max_number_of_messages_in_group;

            private int max_number_of_overflow_sms_invites;

            private String email_subject;

            private String email_contents;

            public String getSmsBody() {
                return sms_body;
            }

            public int getMaxNumberOfGroupMessagesToSend() {
                return max_number_of_group_messages_to_send;
            }

            public int getMaxNumberOfMessagesInGroup() {
                return max_number_of_messages_in_group;
            }

            public int getMaxNumberOfOverflowSmsInvites() {
                return max_number_of_overflow_sms_invites;
            }

            public String getEmailSubject() {
                return email_subject;
            }

            public String getEmailContents() {
                return email_contents;
            }

            @Override
            public String toString() {
                return "SmsTest{" +
                        "sms_body='" + sms_body + '\'' +
                        ", max_number_of_group_messages_to_send=" + max_number_of_group_messages_to_send
                        +
                        ", max_number_of_messages_in_group=" + max_number_of_messages_in_group +
                        ", max_number_of_overflow_sms_invites=" + max_number_of_overflow_sms_invites +
                        ", email_subject='" + email_subject + '\'' +
                        ", email_contents='" + email_contents + '\'' +
                        '}';
            }
        }

        public static class CaptureSettings {

            private String capture_string;

            public String getCaptureString() {
                return capture_string;
            }

            @Override
            public String toString() {
                return "CaptureSettings{" +
                        "capture_string='" + capture_string + '\'' +
                        '}';
            }
        }
        //endregion ClientConfig Inner Classes
    }
}
