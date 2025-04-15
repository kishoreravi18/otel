ARG BASE_IMAGE_VERSION
FROM 155577958684.dkr.ecr.ap-south-1.amazonaws.com/dso-devops-java:21-${BASE_IMAGE_VERSION}

# Set up Labels for the image
LABEL Author="@DevOps" \
      Version="1.0" \
      Maintainer="@DevOps"
USER root    
ARG AWS_ENVIRONMENT
ARG JAR
ENV BASE_REPO_DIR=codec-service \
    http_proxy=http://proxy.dso.cadevops.net:3331 \
    https_proxy=http://proxy.dso.cadevops.net:3331 \
    no_proxy=169.254.169.254,localhost,vivriticapital.net,cadevops.net    
RUN apk update && \
    apk add \
        git 
# Copy application
# COPY jars/ /${BASE_REPO_DIR}/
# COPY entrypoint.sh /
# COPY utils/ /etc/

COPY appjar/ /${BASE_REPO_DIR}/
COPY newrelic/ /${BASE_REPO_DIR}/
COPY entrypoint.sh /
COPY fluentd.conf supervisor.conf /etc/


WORKDIR /${BASE_REPO_DIR}
# Install app dependencies.
RUN chmod +x /entrypoint.sh
# RUN chown -R devops-user:devops-user /${BASE_REPO_DIR}/ && \
#     chown -R devops-user:devops-user /entrypoint.sh && \
#     chown -R devops-user:devops-user /etc/fluentd.conf && \
#     chown -R devops-user:devops-user /etc/supervisor.conf

# USER devops-user

# Expose ports
EXPOSE 8080

# Unset proxy after build
ENV http_proxy="" \ 
    https_proxy="" \
    no_proxy=""

ENTRYPOINT /entrypoint.sh